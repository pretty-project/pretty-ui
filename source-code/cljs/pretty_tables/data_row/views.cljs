
(ns pretty-tables.data-row.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [pretty-tables.data-cell.views :as data-cell.views]
              [pretty-tables.data-row.attributes :as data-row.attributes]
              [pretty-tables.data-row.prototypes :as data-row.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-row
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (maps in vector)}
  [row-id {:keys [cells] :as row-props}]
  [:div (data-row.attributes/row-attributes row-id row-props)
        [:div (data-row.attributes/row-body-attributes row-id row-props)
              (letfn [(f0 [_ cell-props] [data-cell.views/view cell-props])]
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
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    row-id row-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount row-id row-props))
                       :reagent-render         (fn [_ row-props] [data-row row-id row-props])}))

(defn view
  ;     :template (string)(opt)
  ;      Default: "repeat(*cell-count*, 1fr)"
  ;     :width (keyword, px or string)(opt)
  ;      Default: :s}]

  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
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
  ; [data-row {...}]
  ;
  ; @usage
  ; [data-row :my-data-row {...}]
  ([row-props]
   [view (random/generate-keyword) row-props])

  ([row-id row-props]
   ; @note (tutorials#parameterizing)
   (fn [_ row-props]
       (let [row-props (pretty-presets.engine/apply-preset            row-id row-props)
             row-props (data-row.prototypes/row-props-prototype row-id row-props)]
            [view-lifecycles row-id row-props]))))
