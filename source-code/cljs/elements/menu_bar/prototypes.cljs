
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.menu-bar.prototypes
    (:require [candy.api :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bar-props
  ; {:orientation (keyword)(opt)}
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :height (keyword)
  ;  :horizontal-align (keyword)
  ;  :orientation (keyword)}
  [{:keys [orientation] :as bar-props}]
  (merge {:font-size   :s
          :height      :xxl
          :orientation :horizontal}
         (if-not (= orientation :vertical)
                 {:horizontal-align :left})
         (param bar-props)))

(defn item-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) item-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:icon-family (keyword)}
  [{:keys [icon] :as item-props}]
  (merge {}
         (if icon {:icon-family :material-icons-filled})
         (param item-props)))
