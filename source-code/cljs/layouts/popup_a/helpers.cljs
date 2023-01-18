
(ns layouts.popup-a.helpers
    (:require [layouts.popup-a.state :as popup-a.state]
              [hiccup.api            :as hiccup]
              [pretty-css.api        :as pretty-css]
              [x.environment.api     :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (letfn [(f [intersecting?] (if intersecting? (swap! popup-a.state/HEADER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! popup-a.state/HEADER-SHADOW-VISIBLE? assoc  popup-id true)))]
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
  (letfn [(f [intersecting?] (if intersecting? (swap! popup-a.state/FOOTER-SHADOW-VISIBLE? dissoc popup-id)
                                               (swap! popup-a.state/FOOTER-SHADOW-VISIBLE? assoc  popup-id true)))]
         (x.environment/setup-intersection-observer! (hiccup/value popup-id "footer-sensor") f)))

(defn footer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  [popup-id]
  (x.environment/remove-intersection-observer! (hiccup/value popup-id "footer-sensor")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:min-width (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [min-width style] :as popup-props}]
  (-> {:data-content-min-width min-width
       :style                  style}
      (pretty-css/border-attributes popup-props)
      (pretty-css/color-attributes  popup-props)
      (pretty-css/indent-attributes popup-props)))
