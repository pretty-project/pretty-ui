
(ns pretty-inputs.header.views
    (:require [fruits.random.api               :as random]
              [pretty-accessories.label.views :as label.views]
              [pretty-accessories.marker.views :as marker.views]
              [pretty-elements.header.views :as header.views]
              [pretty-guides.helper-text.views :as helper-text.views]
              [pretty-guides.error-text.views :as error-text.views]
              [pretty-guides.info-text.views :as info-text.views]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.attributes :as header.attributes]
              [pretty-inputs.header.prototypes :as header.prototypes]
              [pretty-inputs.methods.api       :as pretty-inputs.methods]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:error-text  error-text.views/SHORTHAND-KEY
                    :helper-text helper-text.views/SHORTHAND-KEY
                    :info-text   info-text.views/SHORTHAND-KEY
                    :label       label.views/SHORTHAND-KEY})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:error-text (map)(opt)
  ;  :helper-text (map)(opt)
  ;  :info-text (map)(opt)
  ;  :info-text-visible? (boolean)(opt)
  ;  :label (map)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [id {:keys [error-text helper-text info-text info-text-visible? label marker] :as props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  [:div (header.attributes/outer-attributes id props)
        [:div (header.attributes/inner-attributes id props)
              (if label              [header.views/view      (pretty-subitems/subitem-id id :label)       label])
              (if info-text-visible? [info-text.views/view   (pretty-subitems/subitem-id id :info-text)   info-text])
              (if helper-text        [helper-text.views/view (pretty-subitems/subitem-id id :helper-text) helper-text])
              (if error-text         [error-text.views/view  (pretty-subitems/subitem-id id :error-text)  error-text])
              (if marker             [marker.views/view      (pretty-subitems/subitem-id id :marker)      marker])]])

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
                         :reagent-render         (fn [_ props] [header id props])}))

(defn view
  ; @description
  ; Header element for inputs with optional error text, helper text, and info text.
  ;
  ; @links Implemented accessories
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-core/cljs/pretty-accessories/api.html#marker)
  ;
  ; @links Implemented guides
  ; [Error-text](pretty-core/cljs/pretty-guides/api.html#error-text)
  ; [Helper-text](pretty-core/cljs/pretty-guides/api.html#helper-text)
  ; [Info-text](pretty-core/cljs/pretty-guides/api.html#info-text)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Input guide properties](pretty-core/cljs/pretty-properties/api.html#input-guide-properties)
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
  ; Check out the implemented accessories.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/header.png)
  ; [header {:label       {:content "My header"}
  ;          :helper-text {:content "My helper text"}
  ;          :error-text  {:content "My error text"}
  ;          :info-text   {:content "My info text"}}]
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
             props (header.prototypes/props-prototype                id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
