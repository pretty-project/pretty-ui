
(ns pretty-accessories.cover.views
    (:require [fruits.random.api                   :as random]
              [pretty-accessories.cover.attributes :as cover.attributes]
              [pretty-accessories.cover.prototypes :as cover.prototypes]
              [pretty-accessories.icon.views       :as icon.views]
              [pretty-accessories.label.views      :as label.views]
              [pretty-subitems.api                 :as pretty-subitems]
              [pretty-accessories.methods.api :as pretty-accessories.methods]
              [pretty-accessories.engine.api :as pretty-accessories.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cover
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [id {:keys [icon label] :as props}]
  [:div (cover.attributes/outer-attributes id props)
        [:div (cover.attributes/inner-attributes id props)
              (cond label [label.views/view (pretty-subitems/subitem-id id :label) label]
                    icon  [icon.views/view  (pretty-subitems/subitem-id id :icon)  icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-accessories.engine/accessory-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-accessories.engine/accessory-will-unmount id props))
                         :reagent-render         (fn [_ props] [cover id props])}))

(defn view
  ; @description
  ; Cover accessory for elements.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Overlay properties](pretty-core/cljs/pretty-properties/api.html#overlay-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Transformation properties](pretty-core/cljs/pretty-properties/api.html#transformation-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/cover.png)
  ; [cover {:fill-color :highlight
  ;         :label      {:content "My cover"}
  ;         :opacity    :medium}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-accessories.methods/apply-accessory-shorthand-map  id props {:icon :icon-name :label :content})
             props (pretty-accessories.methods/apply-accessory-preset         id props)
             props (pretty-accessories.methods/import-accessory-dynamic-props id props)
             props (pretty-accessories.methods/import-accessory-state-events  id props)
             props (pretty-accessories.methods/import-accessory-state         id props)
             props (cover.prototypes/props-prototype                          id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
