
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-loader.helpers
    (:require [mid-fruits.candy         :refer [param]]
              [mid-fruits.css           :as css]
              [hiccup.api               :as hiccup]
              [tools.image-loader.state :as state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  [loader-id]
  (swap! state/LOADERS assoc-in [loader-id :loaded?] true))

(defn image-loaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [loader-id]
  (get-in @state/LOADERS [loader-id :loaded?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :uri (string)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :id (keyword)
  ;   :style (map)}
  [loader-id {:keys [class uri]}]
  {:class (param        class)
   :id    (hiccup/value loader-id)
   :style (if-let [image-loaded? (image-loaded? loader-id)]
                  {:background-image (css/url uri)})})
