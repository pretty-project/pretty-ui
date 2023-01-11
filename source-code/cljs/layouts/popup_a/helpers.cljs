
(ns layouts.popup-a.helpers
    (:require [layouts.popup-a.state :as state]
              [hiccup.api            :as hiccup]
              [x.environment.api     :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! state/HEADER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! state/HEADER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (x.environment/setup-intersection-observer! (hiccup/value popup-id "header-sensor") f)))

(defn header-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (x.environment/remove-intersection-observer! (hiccup/value popup-id "header-sensor")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! state/FOOTER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! state/FOOTER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (x.environment/setup-intersection-observer! (hiccup/value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (x.environment/remove-intersection-observer! (hiccup/value popup-id "footer-sensor")))
