
(ns components.error-content.views
    (:require [components.error-content.prototypes :as error-content.prototypes]
              [pretty-elements.api                 :as pretty-elements]
              [random.api                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-occured-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  [_ _]
  [pretty-elements/label {:color            :warning
                          :content          :an-error-occured
                          :font-size        :m
                          :horizontal-align :center
                          :outdent          {:top :xxl}}])

(defn- error-message-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  ; {:content (metamorphic-content)}
  [_ {:keys [content]}]
  [pretty-elements/label {:color            :muted
                          :content          content
                          :horizontal-align :center
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
   (fn [_ content-props] ; XXX#0106 (README.md#parametering)
       (let [] ; content-props (error-content.prototypes/content-props-prototype content-props)
            [error-content content-id content-props]))))
