
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [pretty-accessories.api                 :as pretty-accessories]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-subitems.api                    :as pretty-subitems]
              [pretty-models.api :as pretty-models]
              [pretty-elements.methods.api        :as pretty-elements.methods]
              [reagent.core                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge cover icon label marker tooltip] :as props}]
  [:div (icon-button.attributes/outer-attributes id props)
        [(pretty-models/clickable-model-auto-tag props) (icon-button.attributes/inner-attributes id props)
         (if icon    [pretty-accessories/icon   (pretty-subitems/subitem-id id :icon)   icon])
         (if label   [pretty-accessories/label  (pretty-subitems/subitem-id id :label)  label])
         (if badge   [pretty-accessories/badge  (pretty-subitems/subitem-id id :badge)  badge])
         (if marker  [pretty-accessories/marker (pretty-subitems/subitem-id id :marker) marker])
         (if cover   [pretty-accessories/cover  (pretty-subitems/subitem-id id :cover)  cover])
         (if tooltip [pretty-accessories/cover  (pretty-subitems/subitem-id id :cover)  cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   id props %))
                         :reagent-render         (fn [_ props] [icon-button id props])}))

(defn view
  ; @description
  ; Icon button element with optional keypress control, timeout lock, and progress display.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress control properties](pretty-core/cljs/pretty-properties/api.html#keypress-control-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/icon-button.png)
  ; [icon-button {:border-radius {:all :s}
  ;               :fill-color    :highlight
  ;               :icon          {:icon-name :settings}
  ;               :gap           :xxs
  ;               :indent        {:all :xxs}
  ;               :label         {:content "My icon button"}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map   id props {:icon :icon-name :label :content})
             props (pretty-elements.methods/apply-element-preset          id props)
             props (pretty-elements.methods/import-element-timeout-events id props)
             props (pretty-elements.methods/import-element-timeout        id props)
             props (icon-button.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
