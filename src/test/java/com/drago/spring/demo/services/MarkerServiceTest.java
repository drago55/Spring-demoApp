package com.drago.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.MarkerType;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.services.servicesimpl.MarkerServiceImpl;

@Transactional
public class MarkerServiceTest extends ApplicationTests {

	@Autowired
	private MarkerServiceImpl service;
	
	@Test
	@WithMockUser(username = "drago@net.hr", authorities = { "ADMIN" })
	public void testGetMarkerByUserId() {
		//When
		Optional<MarkerDto> marker = service.findMarkerByUserId(1l);
		//Then
		assertThat(marker).isNotNull();
		assertThat(marker.get().getUserFirstName()).isEqualTo("Dragutin");
	}
	
	@Test
	@WithMockUser(username = "drago@net.hr", authorities = { "ADMIN" })
	public void testUpdateMarker() {
		
		//When
		MockMultipartFile[] files = { new MockMultipartFile("1f3bed2c-539e-4239-a9fb-43749ddafb61.jpg",
				"WIN_20190609_20_48_55_Pro.jpg", null, "bar".getBytes()) };
		MarkerDto savedMarker = service.save(getMarkerDto(), files);
		assertThat(savedMarker).isNotNull();
		assertThat(savedMarker.getId()).isNotNull();
		savedMarker.setMarkerName("UpdatedMarker");
		//Then
		MockMultipartFile[] updateFiles = { new MockMultipartFile("NewUpdateImage-539e-4239-a9fb-43749ddafb61.jpg",
						"NewImage.jpg", null, "bar".getBytes()) };
		MarkerDto updateMarker = service.save(savedMarker, updateFiles);
		
		assertThat(updateMarker).isNotNull();
		assertThat(updateMarker.getMarkerName()).isEqualTo(savedMarker.getMarkerName());
		
		assertThat(updateMarker.getImages()).isNotEmpty();

	}

	@Test
	@WithMockUser(username = "drago@net.hr", authorities = { "ADMIN" })
	public void testSaveMarker() {

		MockMultipartFile[] files = { new MockMultipartFile("1f3bed2c-539e-4239-a9fb-43749ddafb61.jpg",
				"WIN_20190609_20_48_55_Pro.jpg", null, "bar".getBytes()) };
		MarkerDto savedMarker = service.save(getMarkerDto(), files);

		assertThat(savedMarker).isNotNull();
		assertThat(savedMarker.getId()).isNotNull();
		assertThat(savedMarker.getImages()).isNotEmpty();

	}
	
	@Test
	public void testFindAllMarkers() {
		Set<MarkerDto> setOfMarkers = service.findAllMarkers();
		assertThat(setOfMarkers).isNotNull();
		assertThat(setOfMarkers).isNotEmpty();
		assertThat(setOfMarkers.size()).isEqualTo(1);
	}
	
	@Test
	public void testFindMarker() {
		assertThat(service.findMarkerById(1l)).isNotNull();
	}
	
	@Test
	public void testFindMarkerThrowsNoSuchMarkerException() {
		assertThatThrownBy(() -> service.findMarkerById(2l))
		.isInstanceOf(NoSuchMarkerException.class).hasMessage("Marker don't exists!");
	}
	
	@Test
	@WithMockUser(username = "drago@net.hr", authorities = { "ADMIN" })
	public void testDeleteMarker() {
		// When
		MockMultipartFile[] files = { new MockMultipartFile("1f3bed2c-539e-4239-a9fb-43749ddafb61.jpg",
				"WIN_20190609_20_48_55_Pro.jpg", null, "bar".getBytes()) };
		MarkerDto savedMarker = service.save(getMarkerDto(), files);
		assertThat(savedMarker).isNotNull();
		// Then
		service.deleteMarkerById(savedMarker.getId());
		
		assertThatThrownBy(() -> service.findMarkerById(savedMarker.getId()))
		.isInstanceOf(NoSuchMarkerException.class).hasMessage("Marker don't exists!");

	}

	private MarkerDto getMarkerDto() {
		MarkerDto marker = new MarkerDto();
		marker.setAddress("4 Ulica Istarskog razvoda");
		marker.setCity("Pula");
		marker.setMarkerName("TestMarker1");
		marker.setState("Hrvatska");
		marker.setUserFirstName("Drago");
		marker.setUserLastName("Horvat");
		marker.setPostalCode("10000");
		marker.setDescription("Opis test markera 1");
		marker.setLat(44.867f);
		marker.setLon(13.864f);
		marker.setMarkerType(MarkerType.TYPE1.getName());

		return marker;

	}

}
