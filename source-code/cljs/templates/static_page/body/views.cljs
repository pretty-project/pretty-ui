
(ns templates.static-page.body.views
    (:require [components.api   :as components]
              [elements.api     :as elements]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) page-id
  ; @param (map) body-props
  ; {:content (metamorphic-content)}
  ;
  ; @usage
  ; [body :my-static-page {...}]
  ;
  ; @usage
  ; (defn my-content [page-id] ...)
  ; [body :my-static-page {:content #'my-content}]
  [page-id {:keys [content]}]
  [x.components/delayer ::body
                        {:content     [:div#t-static-page--body [x.components/content page-id content]]
                         :placeholder [:div#t-static-page--body [components/ghost-view {:layout :box-surface-body}]]
                         :timeout     500}])
