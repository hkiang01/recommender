package com.cs498cloum2pxyv.recommender.data.weather

import java.net.URL

object Config {

  /**
    * ftp://ftp.ncdc.noaa.gov/pub/data/ghcn/daily/readme.txt
    *
    * --------------------------------------------------------------------------------

IV. FORMAT OF "ghcnd-stations.txt"

------------------------------
Variable   Columns   Type
------------------------------
ID            1-11   Character
LATITUDE     13-20   Real
LONGITUDE    22-30   Real
ELEVATION    32-37   Real
STATE        39-40   Character
NAME         42-71   Character
GSN FLAG     73-75   Character
HCN/CRN FLAG 77-79   Character
WMO ID       81-85   Character
------------------------------

These variables have the following definitions:

ID         is the station identification code.  Note that the first two
           characters denote the FIPS  country code, the third character
           is a network code that identifies the station numbering system
           used, and the remaining eight characters contain the actual
           station ID.

           See "ghcnd-countries.txt" for a complete list of country codes.
	   See "ghcnd-states.txt" for a list of state/province/territory codes.

           The network code  has the following five values:

           0 = unspecified (station identified by up to eight
	       alphanumeric characters)
	   1 = Community Collaborative Rain, Hail,and Snow (CoCoRaHS)
	       based identification number.  To ensure consistency with
	       with GHCN Daily, all numbers in the original CoCoRaHS IDs
	       have been left-filled to make them all four digits long.
	       In addition, the characters "-" and "_" have been removed
	       to ensure that the IDs do not exceed 11 characters when
	       preceded by "US1". For example, the CoCoRaHS ID
	       "AZ-MR-156" becomes "US1AZMR0156" in GHCN-Daily
           C = U.S. Cooperative Network identification number (last six
               characters of the GHCN-Daily ID)
	   E = Identification number used in the ECA&D non-blended
	       dataset
	   M = World Meteorological Organization ID (last five
	       characters of the GHCN-Daily ID)
	   N = Identification number used in data supplied by a
	       National Meteorological or Hydrological Center
	   R = U.S. Interagency Remote Automatic Weather Station (RAWS)
	       identifier
	   S = U.S. Natural Resources Conservation Service SNOwpack
	       TELemtry (SNOTEL) station identifier
           W = WBAN identification number (last five characters of the
               GHCN-Daily ID)

LATITUDE   is latitude of the station (in decimal degrees).

LONGITUDE  is the longitude of the station (in decimal degrees).

ELEVATION  is the elevation of the station (in meters, missing = -999.9).


STATE      is the U.S. postal code for the state (for U.S. stations only).

NAME       is the name of the station.

GSN FLAG   is a flag that indicates whether the station is part of the GCOS
           Surface Network (GSN). The flag is assigned by cross-referencing
           the number in the WMOID field with the official list of GSN
           stations. There are two possible values:

           Blank = non-GSN station or WMO Station number not available
           GSN   = GSN station

HCN/      is a flag that indicates whether the station is part of the U.S.
CRN FLAG  Historical Climatology Network (HCN).  There are three possible
          values:

           Blank = Not a member of the U.S. Historical Climatology
	           or U.S. Climate Reference Networks
           HCN   = U.S. Historical Climatology Network station
	   CRN   = U.S. Climate Reference Network or U.S. Regional Climate
	           Network Station

WMO ID     is the World Meteorological Organization (WMO) number for the
           station.  If the station has no WMO number (or one has not yet
	   been matched to this station), then the field is blank.

--------------------------------------------------------------------------------
    *
    */
  val noaaStationFieldsAndIndices: Array[(String, Int, Int)] = Array(
    ("id", 1,11),
    ("latitude", 13,20),
    ("longitude", 22,30),
    ("elevation", 32,37),
    ("state", 39,40),
    ("name", 42,71),
    ("gsn_flag", 73,75),
    ("hcn_crn_flag", 77,79),
    ("wmo_id", 81,85)
  )

  case class NoaaStation(
    id: String,
    latitude: String,
    longitude: String,
    elevation: String,
    state: String,
    name: String,
    gsn_flag: String,
    hcn_crn_flag: String,
    wmo_id: String
  )

  private def createNoaaStationFromArr(v: Array[String]): NoaaStation = {
    NoaaStation(v.head, v.apply(1), v.apply(2), v.apply(3), v.apply(4), v.apply(5), v.apply(6), v.apply(7), v.apply(8))
  }

  def createNoaaStation(line: String): NoaaStation = {
    createNoaaStationFromArr(
      noaaStationFieldsAndIndices.map(tuple => {
        val end = math.min(tuple._3, line.length)
        line.substring(tuple._2-1, end).replace("\\w+", "")
      }))
  }
}
