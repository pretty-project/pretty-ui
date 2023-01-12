
(ns templates.item-lister.footer.views
    (:require [components.api          :as components]
              [engines.item-lister.api :as item-lister]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; @param (keyword) lister-id
  ; @param (map) footer-props
  ;
  ; @usage
  ; [footer :my-lister {...}]
  [lister-id {}]
  (if-let [data-received? @(r/subscribe [:item-lister/data-received? lister-id])]
          (let [all-item-count    @(r/subscribe [:item-lister/get-all-item-count    lister-id])
                listed-item-count @(r/subscribe [:item-lister/get-listed-item-count lister-id])
                download-info      {:content :npn-items-downloaded :replacements [listed-item-count all-item-count]}]
               [:div#t-item-lister--footer [components/section-description {:content download-info}]])))
