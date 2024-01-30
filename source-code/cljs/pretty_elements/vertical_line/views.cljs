
(ns pretty-elements.vertical-line.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.vertical-line.attributes :as vertical-line.attributes]
              [pretty-elements.vertical-line.prototypes :as vertical-line.prototypes]
              [pretty-elements.engine.api                        :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                              :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-line
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  [:div (vertical-line.attributes/line-attributes line-id line-props)
        [:div (vertical-line.attributes/line-body-attributes line-id line-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    line-id line-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount line-id line-props))
                       :reagent-render         (fn [_ line-props] [vertical-line line-id line-props])}))

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   Default: :muted
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1
  ;  :theme (keyword)(opt)}
  ;
  ; + attol még lehet a container-nek width tulajdonsága, hogy a vonal vastagságát a strength határozzta meg
  ;   csak középre kell benne igazítani a vonalat
  ;
  ; indent?
  ;
  ; @usage
  ; [vertical-line {...}]
  ;
  ; @usage
  ; [vertical-line :my-vertical-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   ; @note (tutorials#parametering)
   (fn [_ line-props]
       (let [line-props (pretty-presets.engine/apply-preset            line-id line-props)
             line-props (vertical-line.prototypes/line-props-prototype line-id line-props)]
            [element-lifecycles line-id line-props]))))
