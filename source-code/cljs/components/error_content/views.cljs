
(ns components.error-content.views
    (:require [components.error-content.prototypes :as error-content.prototypes]
              [fruits.random.api                   :as random]
              [pretty-elements.api                 :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-occured-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  [_ _])
  ;[pretty-elements/label {:color            :warning
  ;                        :content          :an-error-occured
  ;                        :font-size        :m
  ;                        :horizontal-align :center
  ;                        :outdent          {:top :xxl}}])

(defn- error-message-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  ; {:content (metamorphic-content)}
  [_ {:keys [content]}])
  ;[pretty-elements/label {:color            :muted
  ;                        :content          content
  ;                        :horizontal-align :center
  ;                        :outdent          {:bottom :xxl}]}])
;
(defn- error-content
  ; @param (keyword) content-id
  ; @param (map) content-props
  [content-id content-props]
  [:<> [error-occured-label content-id content-props]
       [error-message-label content-id content-props]])

(defn view
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
   [view (random/generate-keyword) content-props])

  ([content-id content-props]
   ; @note (tutorials#parameterizing)
   (fn [_ content-props]
       (let [] ; content-props (error-content.prototypes/content-props-prototype content-props)
            [error-content content-id content-props]))))
