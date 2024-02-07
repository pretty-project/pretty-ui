
(ns pretty-website.scroll-icon.attributes
    (:require [pretty-attributes.api            :as pretty-attributes]
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
  ; {:color (string)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [color] :as icon-props}]
  (-> {:class :pw-scroll-icon--body
       :style {"--icon-color" color}}
      (pretty-attributes/style-attributes icon-props)))

      ; indent?

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
      (pretty-attributes/class-attributes  icon-props)
      (pretty-attributes/outdent-attributes icon-props)
      (pretty-attributes/state-attributes  icon-props)
      (pretty-attributes/theme-attributes   icon-props)))
