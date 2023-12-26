
(ns pretty-website.scroll-icon.attributes
    (:require [pretty-css.api                   :as pretty-css]
              [pretty-website.scroll-icon.state :as scroll-icon.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {}
  [icon-id _]
  {:callback-f (fn [intersecting?] (swap! scroll-icon.state/ICON-VISIBLE? assoc icon-id intersecting?))
   :style {:left "0" :position "absolute" :top "0"}})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:color (string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [color style] :as icon-props}]
  {:class :pw-scroll-icon--body
   :style (assoc style "--icon-color" color)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-visible (boolean)}
  [icon-id icon-props]
  (-> {:class        :pw-scroll-icon
       :data-visible (icon-id @scroll-icon.state/ICON-VISIBLE?)}
      (pretty-css/class-attributes   icon-props)
      (pretty-css/state-attributes   icon-props)
      (pretty-css/outdent-attributes icon-props)))
