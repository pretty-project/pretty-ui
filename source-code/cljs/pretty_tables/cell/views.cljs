
(ns pretty-tables.cell.views
    (:require [fruits.random.api                  :as random]
              [pretty-elements.engine.api         :as pretty-elements.engine]
              [pretty-presets.engine.api          :as pretty-presets.engine]
              [pretty-tables.cell.attributes :as cell.attributes]
              [pretty-tables.cell.prototypes :as cell.prototypes]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cell
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [cell-id {:keys [content] :as cell-props}]
  [:div (cell.attributes/cell-attributes cell-id cell-props)
        [:div (cell.attributes/cell-body-attributes cell-id cell-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  [cell-id cell-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    cell-id cell-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount cell-id cell-props))
                         :reagent-render         (fn [_ cell-props] [cell cell-id cell-props])}))

(defn view
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-placeholder (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :line-height (keyword, px or string)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; flex-properties
  ;
  ; @usage
  ; [cell {...}]
  ;
  ; @usage
  ; [cell :my-cell {...}]
  ([cell-props]
   [view (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cell-props]
       (let [cell-props (pretty-presets.engine/apply-preset   cell-id cell-props)
             cell-props (cell.prototypes/cell-props-prototype cell-id cell-props)]
            [view-lifecycles cell-id cell-props]))))
