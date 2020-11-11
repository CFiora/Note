package com.fiora.note2.dao;

import com.fiora.note2.model.NetDisk;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetDiskDao extends JpaRepository<NetDisk, Long> {

    @Query(nativeQuery = true, value="select * from net_disk where name like '%:filter%' or path like '%:filter%'")
    public List<NetDisk> findByNameOrPath(@Param("filter") String filter);
}
