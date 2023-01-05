
(ns templates.static-page.footer.views
    (:require [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; @param (keyword) page-id
  ; @param (map) footer-props
  ; {:content (metamorphic-content)}
  ;
  ; @usage
  ; [footer :my-static-page {...}]
  [page-id {:keys [content]}]
  [x.components/delayer ::footer
                        {:content [:div#t-static-page--footer [x.components/content page-id content]]
                         :timeout 500}])
