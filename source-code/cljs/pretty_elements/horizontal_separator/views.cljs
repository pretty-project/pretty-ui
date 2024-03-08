
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [pretty-accessories.api                          :as pretty-accessories]
              [pretty-elements.engine.api                      :as pretty-elements.engine]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-presets.engine.api                       :as pretty-presets.engine]
              [pretty-subitems.api                             :as pretty-subitems]
              [reagent.core                                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:label (map)(opt)
  ;  ...}
  [id {:keys [label] :as props}]
  [:div (horizontal-separator.attributes/outer-attributes id props)
        [:div (horizontal-separator.attributes/inner-attributes id props)
              (when :always [:hr (horizontal-separator.attributes/line-attributes id props)])
              (when label   [pretty-accessories/label (pretty-subitems/subitem-id id :label) label])
              (when :always [:hr (horizontal-separator.attributes/line-attributes id props)])]])

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
                         :reagent-render         (fn [_ props] [horizontal-separator id props])}))

(defn view
  ; @description
  ; Horizontal line element with optional label.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Line properties](pretty-core/cljs/pretty-properties/api.html#line-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/horizontal-separator.png)
  ; [horizontal-separator {:gap        :xs
  ;                        :label      {:content "My horizontal separator" :text-color :muted}
  ;                        :line-color :muted}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset              id props)
             props (horizontal-separator.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
