
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.csrf
   (:require [ajax.config      :as config]
             [ajax.core        :as core]
             [ajax.helpers     :as helpers]
             [mid-fruits.candy :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-headers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (if (helpers/request->local-request? request)
      (update request :headers merge {"x-csrf-token" config/CSRF-TOKEN})
      (return request)))

(defn load-interceptors!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (swap! core/default-interceptors conj (core/to-interceptor {:name "default headers" :request default-headers})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; ...
(load-interceptors!)
