package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.SandboxEntity;
import net.jewczuk.openbip.to.SandboxTO;

@Component
public class SandboxMapper {

	public SandboxTO map2TO(SandboxEntity entity) {
		return new SandboxTO(entity.getTitle(), entity.getLink(), entity.getContent());
	}
}
