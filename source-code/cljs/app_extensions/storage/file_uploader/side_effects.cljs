
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.side-effects
    (:require [app-fruits.dom  :as dom]
              [x.app-core.api  :as a]
              [x.app-tools.api :as tools]
              [app-extensions.storage.file-uploader.views :as file-uploader.views]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  (tools/append-temporary-component! [file-uploader.views/file-selector uploader-id uploader-props]
                                    #(-> "storage--file-selector" dom/get-element-by-id .click)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :storage.file-uploader/open-file-selector! open-file-selector!)
