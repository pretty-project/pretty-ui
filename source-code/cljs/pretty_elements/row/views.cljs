
(ns pretty-elements.row.views
    (:require [fruits.random.api              :as random]
              [pretty-elements.engine.api     :as pretty-elements.engine]
              [pretty-elements.row.attributes :as row.attributes]
              [pretty-elements.row.prototypes :as row.prototypes]
              [pretty-presets.engine.api      :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:content (metamorphic-content)(opt)
  ;  ...}
  [row-id {:keys [content] :as row-props}]
  [:div (row.attributes/row-attributes row-id row-props)
        [:div (row.attributes/row-body-attributes row-id row-props)
              (-> content)]])

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
                         :reagent-render         (fn [_ row-props] [row row-id row-props])}))

(defn view
  ; @description
  ; Horizontal flex container element.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Content properties](pretty-core/cljs/pretty-properties/api.html#content-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; Check out the implemented properties.
  ;
  ; @usage (row.png)
  ; [row {:border-color     :primary
  ;       :border-radius    {:all :m}
  ;       :border-width     :xs
  ;       :content          [:<> [:div "My column #1"]
  ;                              [:div "My column #2"]
  ;                              [:div "My column #3"]]
  ;       :fill-color       :highlight
  ;       :gap              :xs
  ;       :horizontal-align :center
  ;       :vertical-align   :center
  ;       :height           :s
  ;       :width            :5xl}]
  ([row-props]
   [view (random/generate-keyword) row-props])

  ([row-id row-props]
   ; @note (tutorials#parameterizing)
   (fn [_ row-props]
       (let [row-props (pretty-presets.engine/apply-preset row-id row-props)
             row-props (row.prototypes/row-props-prototype row-id row-props)]
            [view-lifecycles row-id row-props]))))
