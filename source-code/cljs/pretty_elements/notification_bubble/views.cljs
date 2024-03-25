
(ns pretty-elements.notification-bubble.views
    (:require [fruits.random.api                              :as random]
              [pretty-elements.adornment-group.views          :as adornment-group.views]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-elements.methods.api                    :as pretty-elements.methods]
              [pretty-elements.notification-bubble.attributes :as notification-bubble.attributes]
              [pretty-elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [pretty-subitems.api                            :as pretty-subitems]
              [reagent.core                                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-KEY :content)
(def SHORTHAND-MAP {:end-adornment-group   adornment-group.views/SHORTHAND-MAP
                    :start-adornment-group adornment-group.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-bubble
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:content (multitype-content)(opt)
  ;  :end-adornment-group (map)(opt)
  ;  :start-adornment-group (map)(opt)
  ;  ...}
  [id {:keys [content end-adornment-group start-adornment-group] :as props}]
  [:div (notification-bubble.attributes/outer-attributes id props)
        [:div (notification-bubble.attributes/inner-attributes id props)
              (if start-adornment-group [adornment-group.views/view (pretty-subitems/subitem-id id :start-adornment-group) start-adornment-group])
              (if content               [:div (notification-bubble.attributes/body-attributes id props) content])
              (if end-adornment-group   [adornment-group.views/view (pretty-subitems/subitem-id id :end-adornment-group) end-adornment-group])]])

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
                         :reagent-render         (fn [_ props] [notification-bubble id props])}))

(defn view
  ; @description
  ; Notification bubble element with optional adornments and progress display.
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
  ;
  ; @usage (pretty-elements/notification-bubble.png)
  ; [notification-bubble {:border-radius       {:all :m}
  ;                       :content             "My notification bubble #1"
  ;                       :fill-color          :primary
  ;                       :indent              {:horizontal :s}
  ;                       :outer-height        :xs
  ;                       :outer-width         :3xl
  ;                       :end-adornment-group {:adornment-default {:fill-color :highlight :border-radius {:all :s}}
  ;                                             :adornments        [{:icon {:icon-name :close}}]}}]
  ;
  ; [notification-bubble {:border-color          :highlight
  ;                       :border-radius         {:all :m}
  ;                       :content               "My notification bubble #2"
  ;                       :fill-color            :highlight
  ;                       :indent                {:horizontal :s}
  ;                       :outer-height          :xs
  ;                       :outer-width           :3xl
  ;                       :start-adornment-group {:adornment-default {:fill-color :default :border-color :highlight :border-radius {:all :s}}
  ;                                               :adornments        [{:icon {:icon-name :close}}]}}]
  ;
  ; @usage
  ; ;; The shorthand form of the property map is perceived as the ':content' property.
  ; [notification-bubble "My notification bubble"]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-key  id props SHORTHAND-KEY)
             props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-presets        id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (notification-bubble.prototypes/props-prototype       id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
