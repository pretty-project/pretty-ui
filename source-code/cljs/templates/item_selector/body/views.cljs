
(ns templates.item-selector.body.views
    (:require [components.api                          :as components]
              [engines.item-browser.api                :as item-browser]
              [engines.item-lister.api                 :as item-lister]
              [re-frame.api                            :as r]
              [templates.item-selector.body.prototypes :as body.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-body
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ; {:list-item-element (component or symbol)}
  [selector-id {:keys [list-item-element]}]
  (let [downloaded-items @(r/subscribe [:item-lister/get-downloaded-items selector-id])
        selector-props   @(r/subscribe [:x.db/get-item [:engines :engine-handler/meta-items selector-id]])]
       (letfn [(f [item-list item-dex item]
                  (conj item-list [list-item-element selector-id selector-props item-dex item]))]
              (reduce-kv f [:<>] downloaded-items))))

(defn- item-list
  ; @param (keyword) selector-id
  ; @param (map) body-props
  [selector-id body-props]
  [:div#t-item-lister--list-body [item-list-body selector-id body-props]])

(defn- item-selector
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ; {:engine (keyword)(opt)}
  [selector-id {:keys [engine] :as body-props}]
  (let [body-props (assoc body-props :error-element [components/error-content {:content :the-content-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :item-list :item-count 3}]
                                     :list-element  [item-list selector-id body-props])]))
       ;(case engine :item-browser [item-browser/body selector-id body-props]
        ;            :item-lister  [item-lister/body  selector-id body-props]
        ;                          [item-lister/body  selector-id body-props])]))

(defn body
  ; A komponens további paraméterezését az engines.item-lister/body
  ; és az engines.item-browser/body komponens dokumentácójában találod!
  ;
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ; {:default-order-by (namespaced keyword)(opt)
  ;   Default: :modified-at/descending
  ;  :engine (keyword)(opt)
  ;   :item-browser, :item-lister
  ;   Default: :item-lister
  ;  :list-item-element (component or symbol)}
  ;
  ; @usage
  ; [body :my-selector {...}]
  ;
  ; @usage
  ; (defn my-list-item-element [selector-id selector-props item-dex item] ...)
  ; [body :my-selector {:list-item-element #'my-list-item-element}]
  [selector-id body-props]
  (let [body-props (body.prototypes/body-props-prototype selector-id body-props)]
       [:div#t-item-selector--body [item-selector selector-id body-props]]))
