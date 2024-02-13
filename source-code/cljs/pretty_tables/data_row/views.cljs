
(ns pretty-tables.data-row.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [pretty-tables.data-cell.views     :as data-cell.views]
              [pretty-tables.data-row.attributes :as data-row.attributes]
              [pretty-tables.data-row.prototypes :as data-row.prototypes]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-row-cell
  ; @ignore
  ;
  ; @param (integer) cell-dex
  ; @param (map) cell-props
  [cell-dex cell-props]
  (let [cell-props (data-row.prototypes/cell-props-prototype cell-dex cell-props)]
       [data-cell.views/view cell-props]))

(defn- data-row
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (maps in vector)}
  [row-id {:keys [cells] :as row-props}]
  [:div (data-row.attributes/row-attributes row-id row-props)
        [:div (data-row.attributes/row-body-attributes row-id row-props)
              (letfn [(f0 [cell-dex cell-props] [data-row-cell cell-dex cell-props])]
                     (hiccup/put-with-indexed [:<>] cells f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  [row-id row-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    row-id row-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount row-id row-props))
                         :reagent-render         (fn [_ row-props] [data-row row-id row-props])}))

(defn view
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; {:cell-default (map)(opt)
  ;  :cells (maps in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :row-template (keyword or string)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [data-row {...}]
  ;
  ; @usage
  ; [data-row :my-data-row {...}]
  ([row-props]
   [view (random/generate-keyword) row-props])

  ([row-id row-props]
   ; @note (tutorials#parameterizing)
   (fn [_ row-props]
       (let [row-props (pretty-presets.engine/apply-preset                            row-id row-props)
             row-props (data-row.prototypes/row-props-prototype                       row-id row-props)
             row-props (pretty-elements.engine/element-subitem-group<-subitem-default row-id row-props :cells :cell-default)
             row-props (pretty-elements.engine/element-subitem-group<-disabled-state  row-id row-props :cells)
             row-props (pretty-elements.engine/leave-element-disabled-state           row-id row-props :cells)]
            [view-lifecycles row-id row-props]))))
