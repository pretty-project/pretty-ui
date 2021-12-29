
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.07.26
; Description:
; Version: v1.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.svg
    (:require [mid-fruits.candy :refer [param]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-box
  ; @param (integer) width
  ; @param (integer) height
  ;
  ; @example
  ;  (svg/view-box 1024 1024)
  ;  =>
  ;  "0 0 1024 1024"
  ;
  ; @return (string)
  [width height]
  (str "0 0 " width " " height))

(defn element-attributes
  ; @param (map) attributes
  ;
  ; @usage
  ;  (svg/element-attributes {...})
  ;
  ; @return (map)
  [attributes]
  (merge {}
         (param attributes)))

(defn wrapper-attributes
  ; @param (map) attributes
  ;  {:height (px)
  ;   :style (map)(opt)
  ;   :width (px)}
  ;
  ; @example
  ;  (svg/wrapper-attributes {:height 256 :width 256})
  ;  =>
  ;  {:style {:height "100%" :width "100%"}
  ;   :view-box "0 0 256 256"
  ;   :xmlns "..."}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:height (string)
  ;     :width (string)}
  ;   :view-box (string)
  ;   :xmlns (string)}
  [{:keys [height style width] :as attributes}]
  (merge (dissoc attributes :height :width)
         (param  attributes)
         {:style    (merge {:height "100%" :width "100%"} style)
          :view-box (view-box width height)
          :xmlns    "http://www.w3.org/2000/svg"}))