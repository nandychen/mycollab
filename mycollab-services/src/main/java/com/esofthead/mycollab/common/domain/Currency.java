/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
/*Domain class of table s_currency*/
package com.esofthead.mycollab.common.domain;

import com.esofthead.mycollab.core.arguments.ValuedBean;

@SuppressWarnings("ucd")
@com.esofthead.mycollab.core.db.metadata.Table("s_currency")
public class Currency extends ValuedBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.id
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @com.esofthead.mycollab.core.db.metadata.Column("id")
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.shortname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @org.hibernate.validator.constraints.Length(max=45, message="Field value is too long")
    @com.esofthead.mycollab.core.db.metadata.Column("shortname")
    private String shortname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.isocode
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @org.hibernate.validator.constraints.Length(max=45, message="Field value is too long")
    @com.esofthead.mycollab.core.db.metadata.Column("isocode")
    private String isocode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.symbol
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @org.hibernate.validator.constraints.Length(max=45, message="Field value is too long")
    @com.esofthead.mycollab.core.db.metadata.Column("symbol")
    private String symbol;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.conversionrate
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @com.esofthead.mycollab.core.db.metadata.Column("conversionrate")
    private Double conversionrate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column s_currency.fullname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    @org.hibernate.validator.constraints.Length(max=100, message="Field value is too long")
    @com.esofthead.mycollab.core.db.metadata.Column("fullname")
    private String fullname;

    private static final long serialVersionUID = 1;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.id
     *
     * @return the value of s_currency.id
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.id
     *
     * @param id the value for s_currency.id
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.shortname
     *
     * @return the value of s_currency.shortname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.shortname
     *
     * @param shortname the value for s_currency.shortname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.isocode
     *
     * @return the value of s_currency.isocode
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public String getIsocode() {
        return isocode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.isocode
     *
     * @param isocode the value for s_currency.isocode
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.symbol
     *
     * @return the value of s_currency.symbol
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.symbol
     *
     * @param symbol the value for s_currency.symbol
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.conversionrate
     *
     * @return the value of s_currency.conversionrate
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public Double getConversionrate() {
        return conversionrate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.conversionrate
     *
     * @param conversionrate the value for s_currency.conversionrate
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setConversionrate(Double conversionrate) {
        this.conversionrate = conversionrate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column s_currency.fullname
     *
     * @return the value of s_currency.fullname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column s_currency.fullname
     *
     * @param fullname the value for s_currency.fullname
     *
     * @mbggenerated Sat Feb 28 16:29:57 ICT 2015
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public static enum Field {
        id,
        shortname,
        isocode,
        symbol,
        conversionrate,
        fullname;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}