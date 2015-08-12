/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.persistence.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchResults {

  int count;

  List results;

  String searchString;

  int firstResult;

  int resultsPerPage;

  public SearchResults() {

  }

  public SearchResults(int c, List res) {

    count = c;
    results = res;
  }

  public int getCount() {

    return count;
  }

  public void setCount(int count) {

    this.count = count;
  }

  public List getResults() {

    return results;
  }

  public void setResults(List results) {

    this.results = results;
  }

  public static SearchResults instance(int c, List res) {

    return new SearchResults(c, res);
  }

  public static SearchResults instance() {

    return new SearchResults();
  }

  public int getFirstResult() {

    return firstResult;
  }

  public void setFirstResult(int firstResult) {

    this.firstResult = firstResult;
  }

  public int getRemainingCount() {

    int value = 0;

    if (results == null)
      value = getCount() - getFirstResult();
    else
      value = getCount() - getFirstResult() - getResults().size();

    return value;
  }

  public int getResultsPerPage() {

    return resultsPerPage;
  }

  public void setResultsPerPage(int resultsPerPage) {

    this.resultsPerPage = resultsPerPage;
  }

  public String getSearchString() {

    return searchString;
  }

  public void setSearchString(String searchString) {

    this.searchString = searchString;
  }

  public String toString() {

    return ToStringBuilder.reflectionToString(this);
  }

  public static SearchResults fromList(List<?> results) {

    SearchResults res = new SearchResults();
    res.setResults(results);
    res.setCount(results.size());
    res.setFirstResult(0);
    res.setResultsPerPage(results.size());

    return res;

  }

}
