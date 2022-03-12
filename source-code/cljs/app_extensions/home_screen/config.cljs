
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.home-screen.config)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people   :on-click [:router/go-to! "/@app-home/clients"]  :badge-color :secondary}
            {:label :products     :icon :category :on-click [:router/go-to! "/@app-home/products"] :badge-color :secondary}
            {:label :file-storage :icon :folder   :on-click [:router/go-to! "/@app-home/storage"]}])
