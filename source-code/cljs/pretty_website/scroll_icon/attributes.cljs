
(ns pretty-website.scroll-icon.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-website.scroll-icon.state :as scroll-icon.state]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.appearance.api :as pretty-css.appearance]))

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
      (pretty-css.basic/style-attributes icon-props)))

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
      (pretty-css.basic/class-attributes   icon-props)
      (pretty-css.layout/outdent-attributes icon-props)
      (pretty-css.basic/state-attributes   icon-props)
      (pretty-css.appearance/theme-attributes   icon-props)))
