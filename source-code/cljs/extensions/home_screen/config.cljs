
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.home-screen.config)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people        :on-click [:router/go-to! "/@app-home/clients"]      :badge-color :secondary}
            {:label :price-quotes :icon :request_quote :on-click [:router/go-to! "/@app-home/price-quotes"] :badge-color :secondary}
            {:label :file-storage :icon :folder        :on-click [:router/go-to! "/@app-home/storage"]}])
