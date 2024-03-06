
(ns pretty-inputs.header.views
    (:require [dynamic-props.api               :as dynamic-props]
              [fruits.random.api               :as random]
              [pretty-accessories.api          :as pretty-accessories]
              [pretty-elements.api             :as pretty-elements]
              [pretty-guides.api               :as pretty-guides]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.attributes :as header.attributes]
              [pretty-inputs.header.prototypes :as header.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [pretty-subitems.api             :as pretty-subitems]
              [reagent.core                    :as reagent]))

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
              (if label              [pretty-elements/header    (pretty-subitems/subitem-id id :label)       label])
              (if info-text-visible? [pretty-guides/info-text   (pretty-subitems/subitem-id id :info-text)   info-text])
              (if helper-text        [pretty-guides/helper-text (pretty-subitems/subitem-id id :helper-text) helper-text])
              (if error-text         [pretty-guides/error-text  (pretty-subitems/subitem-id id :error-text)  error-text])
              (if marker             [pretty-accessories/marker (pretty-subitems/subitem-id id :marker)      marker])]])

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
  ; Header element for inputs with optional helper text, info text and automatically subscribed error text.
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
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/header.png)
  ; [header {:label       {:content "My header"}
  ;          :helper-text {:content "My helper text"}
  ;          :error-text  {:content "My error text"}
  ;          :info-text   {:content "My info text"}}]
  ;
  ; @usage
  ; ;; To associate a header to an input (to subscribe to its error text), use the same ID for both.
  ; [:<> [header     :my-field {...}]
  ;      [text-field :my-field {...}]]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset           id props)
             props (header.prototypes/props-prototype            id props)
             props (dynamic-props/import-props                   id props)
             props (pretty-inputs.engine/import-input-error-text id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
