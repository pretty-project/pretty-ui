
(ns pretty-inputs.header.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.api             :as pretty-elements]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.adornments :as header.adornments]
              [pretty-inputs.header.attributes :as header.attributes]
              [pretty-inputs.header.prototypes :as header.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-guides.api :as pretty-guides]
              [pretty-subitems.api :as pretty-subitems]
              [pretty-elements.api :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- info-text-adornment
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  (let [toggle-info-text-adornment (header.adornments/toggle-info-text-adornment header-id header-props)]
       [pretty-elements/adornment toggle-info-text-adornment]))

(defn- header-label
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  (let [label-id (pretty-subitems/subitem-id header-id :label)
        label-props (header.prototypes/label-props-prototype header-id header-props)]
       [pretty-elements/label label-id label-props]))

(defn- header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:error-text (map)(opt)
  ;  :helper-text (map)(opt)
  ;  :info-text (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  ...}
  [header-id {:keys [error-text helper-text info-text label] :as header-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  [:div (header.attributes/header-attributes header-id header-props)
        [:div (header.attributes/header-inner-attributes header-id header-props)
              (let [info-text-visible? (pretty-inputs.engine/input-info-text-visible? header-id header-props)]
                   [:<> (if label              [header-label              header-id header-props])
                        (if info-text-visible? [pretty-guides/info-text   header-id info-text])
                        (if helper-text        [pretty-guides/helper-text header-id helper-text])
                        (if error-text         [pretty-guides/error-text  header-id error-text])])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    header-id header-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount header-id header-props))
                         :reagent-render         (fn [_ header-props] [header header-id header-props])}))

(defn view
  ; @description
  ; Header element for inputs with helper text, info text and automatically subscribed error text.
  ;
  ; @links Implemented accessories
  ; [Marker](pretty-core/cljs/pretty-accessories/api.html#marker)
  ;
  ; @links Implemented elements
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
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; Check out the implemented accessories.
  ; Check out the implemented guides.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-inputs/header.png)
  ; [header {}]


  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;
  ; @usage
  ; [header {...}]
  ;
  ; @usage
  ; [header :my-header {...}]
  ;
  ; @usage
  ; ;; All inputs implement the 'header' element
  ; ;; To associate a header with an input, use the same ID for both .
  ; [:<> [header     :my-field {...}]
  ;      [text-field :my-field {...}]]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (pretty-presets.engine/apply-preset           header-id header-props)
             header-props (header.prototypes/header-props-prototype     header-id header-props)
             header-props (pretty-inputs.engine/import-input-error-text header-id header-props)]
            [view-lifecycles header-id header-props]))))
