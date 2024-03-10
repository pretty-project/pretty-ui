
(ns pretty-tables.table.views
    (:require [fruits.hiccup.api              :as hiccup]
              [fruits.random.api              :as random]
              [pretty-elements.engine.api     :as pretty-elements.engine]
              [pretty-elements.methods.api :as pretty-elements.methods]
              [pretty-tables.row.views        :as row.views]
              [pretty-tables.table.attributes :as table.attributes]
              [pretty-tables.table.prototypes :as table.prototypes]
              [reagent.core                   :as reagent]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:rows (maps in vector)(opt)
  ;  ...}
  [id {:keys [rows] :as props}]
  [:div (table.attributes/outer-attributes id props)
        [:div (table.attributes/inner-attributes id props)
              (letfn [(f0 [dex row] [row.views/view (pretty-subitems/subitem-id id dex) row])]
                     (hiccup/put-with-indexed [:<>] rows f0))]])

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
                         :reagent-render         (fn [_ props] [table id props])}))

(defn view
  ; @description
  ; Table element.
  ;
  ; @links Implemented elements
  ; [Row](pretty-ui/cljs/pretty-tables/api.html#row)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-tables/table.png)
  ; [table {:border-crop   :auto
  ;         :border-color  :muted
  ;         :border-radius {:all :m}
  ;         :fill-color    :muted
  ;         :gap           :micro
  ;         :row-default   {:column-gap :micro :cell-default {:fill-color :highlight :outer-height :xs}}
  ;         :rows          [{:cells [{:content "My cell #1.1"} {:content "My cell #1.2"}]}
  ;                         {:cells [{:content "My cell #2.1"} {:content "My cell #2.2"}]}
  ;                         {:cells [{:content "My cell #3.1"} {:content "My cell #3.2"}]}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (table.prototypes/props-prototype                     id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
