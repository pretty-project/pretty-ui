
(ns components.error-content.views
    (:require [components.error-content.prototypes :as error-content.prototypes]
              [elements.api                        :as elements]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-occured-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  [_ _]
  [elements/label {:color            :warning
                   :content          :an-error-occured
                   :font-size        :m
                   :horizontal-align :center
                   :line-height      :block
                   :outdent          {:top :xxl}}])

(defn- error-message-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  ; {:content (metamorphic-content)}
  [_ {:keys [content]}]
  [elements/label {:color            :muted
                   :content          content
                   :horizontal-align :center
                   :line-height      :block
                   :outdent          {:bottom :xxl}}])

(defn- error-content
  ; @param (keyword) content-id
  ; @param (map) content-props
  [content-id content-props]
  [:<> [error-occured-label content-id content-props]
       [error-message-label content-id content-props]])

(defn component
  ; @param (keyword)(opt) content-id
  ; @param (map) content-props
  ; {:content (metamorphic-content)}
  ;
  ; @usage
  ; [error-content {...}]
  ;
  ; @usage
  ; [error-content :my-error-content {...}]
  ([content-props]
   [component (random/generate-keyword) content-props])

  ([content-id content-props]
   (let [] ; content-props (error-content.prototypes/content-props-prototype content-props)
        [error-content content-id content-props])))
