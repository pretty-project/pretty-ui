
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn loader-id->observer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @example
  ;  (engine/loader-id->observer-id :my-loader)
  ;  =>
  ;  :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))
