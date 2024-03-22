
(ns pretty-inputs.select.views
    (:require [fruits.hiccup.api                 :as hiccup]
              [fruits.random.api                 :as random]
              [pretty-elements.button.views      :as button.views]
              [pretty-elements.icon-button.views :as icon-button.views]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-inputs.header.views        :as header.views]
              [pretty-inputs.methods.api         :as pretty-inputs.methods]
              [pretty-inputs.option-group.views  :as option-group.views]
              [pretty-inputs.select-button.views :as select-button.views]
              [pretty-inputs.select.attributes   :as select.attributes]
              [pretty-inputs.select.prototypes   :as select.prototypes]
              [pretty-layouts.popup.views        :as popup.views]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:button        button.views/SHORTHAND-MAP
                    :header        header.views/SHORTHAND-MAP
                    :icon-button   icon-button.views/SHORTHAND-MAP
                    :option-group  option-group.views/SHORTHAND-MAP
                    :popup         popup.views/SHORTHAND-MAP
                    :select-button select-button.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:button (map)(opt)
  ;  :header (map)(opt)
  ;  :icon-button (map)(opt)
  ;  :option-group (map)(opt)
  ;  :popup (map)(opt)
  ;  :popup-visible? (boolean)(opt)
  ;  :select-button (map)(opt)
  ;  ...}
  [id {:keys [button header icon-button option-group popup popup-visible? select-button] :as props}]
  (let [option-group [option-group.views/view (pretty-subitems/subitem-id id :option-group) option-group]
        popup        (assoc-in popup [:body :content] option-group)]
       [:div (select.attributes/outer-attributes id props)
             [:div (select.attributes/inner-attributes id props)
                   (when header         [header.views/view        (pretty-subitems/subitem-id id :header)        header])
                   (cond button         [button.views/view        (pretty-subitems/subitem-id id :button)        button]
                         icon-button    [icon-button.views/view   (pretty-subitems/subitem-id id :icon-button)   icon-button]
                         select-button  [select-button.views/view (pretty-subitems/subitem-id id :select-button) select-button])
                   (when popup-visible? [popup.views/view         (pretty-subitems/subitem-id id :popup)         popup])]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-inputs.engine/pseudo-input-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-inputs.engine/pseudo-input-will-unmount id props))
                         :reagent-render         (fn [_ props] [select id props])}))

(defn view
  ; @description
  ; Select style input.
  ;
  ; @links Implemented elements
  ; [Button](pretty-core/cljs/pretty-elements/api.html#button)
  ; [Icon-button](pretty-core/cljs/pretty-elements/api.html#icon-button)
  ;
  ; @links Implemented inputs
  ; [Header](pretty-core/cljs/pretty-inputs/api.html#header)
  ; [Option-group](pretty-core/cljs/pretty-inputs/api.html#option-group)
  ; [Select-button](pretty-core/cljs/pretty-inputs/api.html#select-button)
  ;
  ; @links Implemented layouts
  ; [Popup](pretty-core/cljs/pretty-layouts/api.html#popup)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented inputs.
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/select.png)
  ; [select {:header {:label       {:content "My select"}
  ;                   :helper-text {:content "My helper text"}
  ;                   :info-text   {:content "My info text"}}
  ;          :option-group {:option-default  {}
  ;                         :option-selected {}
  ;                         :options [{:label {:content "My option #1"}}
  ;                                   {:label {:content "My option #2"}}
  ;                                   {:label {:content "My option #3"}}]}
  ;          :popup {:border-crop :auto :border-radius {:all :s} :overlay {:fill-color :invert}}
  ;          :select-button {:label {:content "My select"}}}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-inputs.methods/apply-input-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-inputs.methods/apply-input-preset         id props)
             props (pretty-inputs.methods/import-input-dynamic-props id props)
             props (pretty-inputs.methods/import-input-state-events  id props)
             props (pretty-inputs.methods/import-input-state         id props)
             props (select.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
