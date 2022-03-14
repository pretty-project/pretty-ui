
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.views
    (:require [app-fruits.reagent                    :as reagent]
              [plugins.view-selector.core.helpers    :as core.helpers]
              [plugins.view-selector.core.prototypes :as core.prototypes]
              [x.app-core.api                        :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) extension-id
  ; @param (map) view-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :default-view-id (keyword)(opt)
  ;    Default: core.config/DEFAULT-VIEW-ID}
  ;
  ; @usage
  ;  [view-selector/view :my-extension {...}]
  ;
  ; @usage
  ;  (defn my-content [extension-id] [:div ...])
  ;  [view-selector/view :my-extension {:content #'my-content}]
  [extension-id {:keys [content] :as view-props}]
  (let [view-props (core.prototypes/view-props-prototype view-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id :view)
                           {:reagent-render      (fn [] [content extension-id])
                            :component-did-mount (fn [] (a/dispatch [:view-selector/view-did-mount extension-id view-props]))})))
