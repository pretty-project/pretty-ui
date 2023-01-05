
(ns templates.item-handler.body.views
    (:require [components.api                         :as components]
              [engines.item-handler.api               :as item-handler]
              [re-frame.api                           :as r]
              [templates.item-handler.body.prototypes :as body.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-structure
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ; {:item-element (component or symbol)
  ;  :item-id (string)}
  [handler-id {:keys [item-element item-id] :as body-props}]
  [:<> [item-handler/downloader handler-id body-props]
       (cond @(r/subscribe [:item-handler/display-error? handler-id])
              [components/error-content {:error :the-item-you-opened-may-be-broken}]
             @(r/subscribe [:item-handler/display-ghost? handler-id body-props])
              [components/ghost-view {:layout :box-surface-body}]
              :item-downloaded
              [item-element])])

(defn body
  ; A komponens további paraméterezését az engines.item-handler.api/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ; {:auto-title? (boolean)(opt)
  ;   Default: true
  ;  :item-element (component or symbol)
  ;  :item-id (string)
  ;  :label-key (keyword)(opt)
  ;   Default: :name
  ;  :suggestion-keys (keywords in vector)(opt)
  ;   Default: [:name]}
  ;
  ; @usage
  ; [body :my-handler {...}]
  ;
  ; @usage
  ; (defn my-item-element [] ...)
  ; [body :my-handler {:item-element #'my-item-element}]
  [handler-id body-props]
  (let [body-props (body.prototypes/body-props-prototype handler-id body-props)]
       [:div#t-item-handler--body [body-structure handler-id body-props]]))
