
(ns pretty-tables.row.views
    (:require [fruits.hiccup.api            :as hiccup]
              [fruits.random.api            :as random]
              [pretty-elements.engine.api   :as pretty-elements.engine]
              [pretty-elements.methods.api :as pretty-elements.methods]
              [pretty-tables.cell.views     :as cell.views]
              [pretty-tables.row.attributes :as row.attributes]
              [pretty-tables.row.prototypes :as row.prototypes]
              [reagent.core                 :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:cells [cell.views/SHORTHAND-KEY]})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:cells (maps in vector)
  ;  ...}
  [id {:keys [cells] :as props}]
  [:div (row.attributes/outer-attributes id props)
        [:div (row.attributes/inner-attributes id props)
              (letfn [(f0 [dex cell] [cell.views/view (pretty-subitems/subitem-id id dex) cell])]
                     (hiccup/put-with-indexed [:<>] cells f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [row id props])}))

(defn view
  ; @description
  ; Table row element.
  ;
  ; @links Implemented elements
  ; [Cell](pretty-ui/cljs/pretty-tables/api.html#cell)
  ;
  ; @links Implemented models
  ; [Grid container model](pretty-core/cljs/pretty-models/api.html#grid-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/row.png)
  ; [row {:border-color  :muted
  ;       :border-crop   :auto
  ;       :border-radius {:all :m}
  ;       :column-gap    :micro
  ;       :fill-color    :muted
  ;       :row-template  :even
  ;       :cell-default  {:outer-height :xs :fill-color :highlight}
  ;       :cells         [{:content "My cell #1"} {:content "My cell #2"}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (row.prototypes/props-prototype                       id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
