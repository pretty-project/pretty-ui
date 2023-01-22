
(ns layouts.struct-popup.helpers
    (:require [layouts.struct-popup.state :as struct-popup.state]
              [hiccup.api                 :as hiccup]
              [x.environment.api          :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! struct-popup.state/HEADER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! struct-popup.state/HEADER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (x.environment/setup-intersection-observer! (hiccup/value popup-id "header-sensor") f)))

(defn header-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (x.environment/remove-intersection-observer! (hiccup/value popup-id "header-sensor")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! struct-popup.state/FOOTER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! struct-popup.state/FOOTER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (x.environment/setup-intersection-observer! (hiccup/value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (x.environment/remove-intersection-observer! (hiccup/value popup-id "footer-sensor")))
