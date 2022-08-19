

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.helpers
    (:require [layouts.popup-a.state :as state]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ;  {:min-width (keyword)(opt)
  ;   :stretch-orientation (keyword)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:data-min-width (keyword)
  ;   :data-stretch-orientation (keyword)
  ;   :style (map)}
  [_ {:keys [min-width stretch-orientation style]}]
  {:data-min-width           min-width
   :data-stretch-orientation stretch-orientation
   :style                    style})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (reset! state/HEADER-SHADOW-VISIBLE? false)
                                               (reset! state/HEADER-SHADOW-VISIBLE? true)))]
         (environment/setup-intersection-observer! (a/dom-value popup-id "header-sensor") f)))

(defn header-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (environment/remove-intersection-observer! (a/dom-value popup-id "header-sensor")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (reset! state/FOOTER-SHADOW-VISIBLE? false)
                                               (reset! state/FOOTER-SHADOW-VISIBLE? true)))]
         (environment/setup-intersection-observer! (a/dom-value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (environment/remove-intersection-observer! (a/dom-value popup-id "footer-sensor")))
