
(ns tools.clipboard.views
    (:require [components.api :as components]
              [elements.api   :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clipboard
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) text
  [text]
  [:input#clipboard {:defaultValue text}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copied-to-clipboard-dialog
  ; @param (string) text
  [text]
  [components/notification-bubble :clipboard/copied-to-clipboard-dialog
                                  {:content [elements/label {:content text :icon :content_copy}]}])
