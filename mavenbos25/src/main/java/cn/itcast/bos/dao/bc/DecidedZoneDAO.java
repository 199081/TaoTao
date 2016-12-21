package cn.itcast.bos.dao.bc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.bc.DecidedZone;

//定区的dao层
public interface DecidedZoneDAO extends JpaRepository<DecidedZone, String>,JpaSpecificationExecutor<DecidedZone>{

}
