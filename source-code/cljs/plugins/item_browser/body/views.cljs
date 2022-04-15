
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.views
    (:require [plugins.item-browser.body.prototypes :as body.prototypes]
              [plugins.item-browser.core.helpers    :as core.helpers]
              [plugins.item-lister.api              :as item-lister]
              [reagent.api                          :as reagent]
              [x.app-core.api                       :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  [item-lister/body browser-id body-props])

(defn body
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :download-limit (integer)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :items-key (keyword)(opt)
  ;    Default: config/DEFAULT-ITEMS-KEY
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :label-key (keyword)(opt)
  ;    Default: config/DEFAULT-LABEL-KEY
  ;   :list-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)(opt)
  ;    Default: config/DEFAULT-PATH-KEY
  ;   :prefilter (map)(opt)
  ;   :root-item-id (string)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-SEARCH-KEYS}
  ;
  ; @example
  ;  [item-browser/body :my-browser {...}]
  ;
  ; @example
  ;  (defn my-list-element [browser-id item-dex item] [:div ...])
  ;  [item-browser/body :my-browser {:list-element #'my-list-element
  ;                                  :prefilter    {:my-type/color "red"}}]
  [browser-id body-props]
  ; Az item-browser body komponensének Reagent azonosítója nem egyezhet meg az item-lister
  ; body komponensének Reagent azonosítójával, mert a két komponens egy időben van a React-fába
  ; csatolva!
  (let [body-props (body.prototypes/body-props-prototype browser-id body-props)]
       (reagent/lifecycles (core.helpers/component-id browser-id :body-wrapper)
                           {:reagent-render         (fn []             [body-structure                  browser-id body-props])
                            :component-did-mount    (fn [] (a/dispatch [:item-browser/body-did-mount    browser-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-browser/body-will-unmount browser-id]))})))
