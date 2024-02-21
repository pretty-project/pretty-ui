
(ns pretty-layouts.popup.utils
    (:require [fruits.hiccup.api                 :as hiccup]
              [intersection-observer.api         :as intersection-observer]
              [pretty-layouts.popup.state :as popup.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f0 [intersecting?] (if intersecting? (swap! popup.state/HEADER-SHADOW-VISIBLE? dissoc popup-id)
                                                (swap! popup.state/HEADER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (intersection-observer/setup-observer! (hiccup/value popup-id "header-sensor") f0)))

(defn header-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (intersection-observer/remove-observer! (hiccup/value popup-id "header-sensor")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f0 [intersecting?] (if intersecting? (swap! popup.state/FOOTER-SHADOW-VISIBLE? dissoc popup-id)
                                                (swap! popup.state/FOOTER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (intersection-observer/setup-observer! (hiccup/value popup-id "footer-sensor") f0)))

(defn footer-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (intersection-observer/remove-observer! (hiccup/value popup-id "footer-sensor")))
