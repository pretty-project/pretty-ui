
(ns pretty-tables.data-column.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [pretty-tables.data-cell.views :as data-cell.views]
              [pretty-tables.data-column.attributes :as data-column.attributes]
              [pretty-tables.data-column.prototypes :as data-column.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-column
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:cells (maps in vector)}
  [column-id {:keys [cells] :as column-props}]
  [:div (data-column.attributes/column-attributes column-id column-props)
        [:div (data-column.attributes/column-body-attributes column-id column-props)
              (letfn [(f0 [_ cell-props] [data-cell.views/view cell-props])]
                     (hiccup/put-with-indexed [:<>] cells f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  [column-id column-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    column-id column-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount column-id column-props))
                       :reagent-render         (fn [_ column-props] [data-column column-id column-props])}))

(defn view
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"
  ;     :width (keyword, px or string)(opt)
  ;      Default: :s}]

  ; @param (keyword)(opt) column-id
  ; @param (map) column-props
  ; {:cells (maps in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [data-column {...}]
  ;
  ; @usage
  ; [data-column :my-data-column {...}]
  ([column-props]
   [view (random/generate-keyword) column-props])

  ([column-id column-props]
   ; @note (tutorials#parameterizing)
   (fn [_ column-props]
       (let [column-props (pretty-presets.engine/apply-preset            column-id column-props)
             column-props (data-column.prototypes/column-props-prototype column-id column-props)]
            [view-lifecycles column-id column-props]))))
