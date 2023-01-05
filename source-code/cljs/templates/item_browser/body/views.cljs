
(ns templates.item-browser.body.views
    (:require [components.api                         :as components]
              [engines.item-browser.api               :as item-browser]
              [re-frame.api                           :as r]
              [templates.item-browser.body.prototypes :as body.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-body
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ; {:list-item-element (component or symbol)}
  [browser-id {:keys [list-item-element] :as body-props}]
  (let [downloaded-items @(r/subscribe [:item-browser/get-downloaded-items browser-id])]
       (letfn [(f [item-list item-dex item]
                  (conj item-list [list-item-element browser-id body-props item-dex item]))]
              (reduce-kv f [:<>] downloaded-items))))

(defn- item-list-header
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ; {:item-list-header (component or symbol)(opt)}
  [browser-id {:keys [item-list-header] :as body-props}]
  (if item-list-header [item-list-header browser-id body-props]))

(defn- item-list
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  [:<> [:div#t-item-lister--list-body   [item-list-body   browser-id body-props]]
       [:div#t-item-lister--list-header [item-list-header browser-id body-props]]])

(defn- item-browser
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error :the-content-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :item-list :item-count 3}]
                                     :list-element  [item-list browser-id body-props])]
       [item-browser/body browser-id body-props]))

(defn body
  ; A komponens további paraméterezését az engines.item-browser.api/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ; {:auto-title? (boolean)(opt)
  ;   Default: true
  ;  :default-order-by (namespaced keyword)(opt)
  ;   Default: :modified-at/descending
  ;  :item-list-header (component or symbol)(opt)
  ;  :label-key (keyword)(opt)
  ;   Default: :name
  ;  :list-item-element (component or symbol)}
  ;
  ; @usage
  ; [body {...}]
  ;
  ; @usage
  ; [body :my-browser {...}]
  ;
  ; @usage
  ; (defn my-item-element [browser-id body-props item-dex item] ...)
  ; [body :my-browser {:item-element  #'my-item-element}]
  ;
  ; @usage
  ; (defn my-item-list-header  [browser-id body-props] ...)
  ; (defn my-list-item-element [browser-id body-props item-dex item] ...)
  ; [body :my-browser {:item-list-header  #'my-item-list-header
  ;                    :list-item-element #'my-list-item-element}]
  [browser-id body-props]
  (let [body-props (body.prototypes/body-props-prototype browser-id body-props)]
       [:div#t-item-lister--body [item-browser browser-id body-props]]))
