
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.helpers
    (:require [layouts.popup-a.state :as state]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-attributes
  [popup-id {:keys [min-width stretch-orientation]}]
  {:data-min-width           min-width
   :data-stretch-orientation stretch-orientation})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (reset! state/HEADER-SHADOW-VISIBLE? false)
                                               (reset! state/HEADER-SHADOW-VISIBLE? true)))]
         (environment/setup-intersection-observer! (a/dom-value popup-id "header-sensor") f)))

(defn header-will-unmount-f
  [popup-id]
  (environment/remove-intersection-observer! (a/dom-value popup-id "header-sensor")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-did-mount-f
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (reset! state/FOOTER-SHADOW-VISIBLE? false)
                                               (reset! state/FOOTER-SHADOW-VISIBLE? true)))]
         (environment/setup-intersection-observer! (a/dom-value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  [popup-id]
  (environment/remove-intersection-observer! (a/dom-value popup-id "footer-sensor")))
