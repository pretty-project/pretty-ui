
(ns pretty-layouts.struct-popup.utils
    (:require [hiccup.api                        :as hiccup]
              [intersection-observer.api         :as intersection-observer]
              [pretty-layouts.struct-popup.state :as struct-popup.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! struct-popup.state/HEADER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! struct-popup.state/HEADER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (intersection-observer/setup-observer! (hiccup/value popup-id "header-sensor") f)))

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
  (letfn [(f [intersecting?] (if intersecting? (swap! struct-popup.state/FOOTER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! struct-popup.state/FOOTER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (intersection-observer/setup-observer! (hiccup/value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (intersection-observer/remove-observer! (hiccup/value popup-id "footer-sensor")))
