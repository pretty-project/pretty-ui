
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.views
    (:require [plugins.view-selector.core.helpers    :as core.helpers]
              [plugins.view-selector.core.prototypes :as core.prototypes]
              [reagent.api                           :as reagent]
              [x.app-core.api                        :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  [extension-id]
  (if-let [content @(a/subscribe [:view-selector/get-body-prop extension-id :content])]
          [content extension-id]))

(defn body
  ; @param (keyword) extension-id
  ; @param (map) body-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :default-view-id (keyword)(opt)
  ;    Default: core.config/DEFAULT-VIEW-ID}
  ;
  ; @usage
  ;  [view-selector/body :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-content [extension-id] [:div ...])
  ;  [view-selector/body :my-extension {:content #'my-content}]
  [extension-id body-props]
  (let [body-props (core.prototypes/body-props-prototype body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id :body)
                           {:reagent-render         (fn []             [body-structure                   extension-id])
                            :component-did-mount    (fn [] (a/dispatch [:view-selector/body-did-mount    extension-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:view-selector/body-will-unmount extension-id]))})))
