
(ns templates.item-browser.footer.views
    (:require [components.api :as components]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; @param (keyword) lister-id
  ; @param (map) browser-props
  ;
  ; @usage
  ; [footer :my-browser {...}]
  [browser-id {}]
  (if-let [data-received? @(r/subscribe [:item-browser/data-received? browser-id])]
          (let [all-item-count        @(r/subscribe [:item-lister/get-all-item-count        browser-id])
                downloaded-item-count @(r/subscribe [:item-lister/get-downloaded-item-count browser-id])
                download-info          {:content :npn-items-downloaded :replacements [downloaded-item-count all-item-count]}]
               [:div#t-item-lister--footer [components/section-description {:content download-info}]])))
