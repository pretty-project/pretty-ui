
(ns tools.infinite-loader.helpers
    (:require [keyword.api                 :as keyword]
              [tools.infinite-loader.state :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn loader-id->observer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @example
  ; (loader-id->observer-id :my-loader)
  ; =>
  ; :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))

(defn observer-paused?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [loader-id]
  (get-in @state/OBSERVERS [loader-id :paused?]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn intersect?
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [loader-id]
  (get-in @state/OBSERVERS [loader-id :intersect?]))
