
(ns templates.item-handler.footer.helpers
    (:require [re-frame.api :as r]
              [string.api   :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-info-label
  ; @param (keyword) handler-id
  ; @param (map) footer-props
  ;
  ; @return (metamorphic-content)
  [handler-id _]
  (let [current-item   @(r/subscribe [:item-handler/get-current-item handler-id])
        modified-at     (-> current-item :modified-at)
        user-first-name (-> current-item :modified-by :user-profile/first-name)
        user-last-name  (-> current-item :modified-by :user-profile/last-name)
        user-full-name @(r/subscribe [:x.locales/get-ordered-name user-first-name user-last-name])
        timestamp      @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        modified        (string/join [user-full-name timestamp] ", " {:join-empty? false})]
       {:content :last-modified-n :replacements [modified]}))
